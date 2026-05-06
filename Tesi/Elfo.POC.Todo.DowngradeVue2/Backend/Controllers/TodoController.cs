using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace TodoApi
{
    [Route("api/[controller]")]
    [ApiController]
    public class TodoController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IConfiguration _config;
        
        // Lista per salvare i JWT validi (whitelist in memoria)
        private static HashSet<string> ValidTokens = new HashSet<string>();

        public TodoController(AppDbContext context, IConfiguration config)
        {
            _context = context;
            _config = config;
        }

        // GET: api/todo
        [HttpGet]
        public async Task<ActionResult<IEnumerable<TodoItem>>> GetTodos()
        {
            var authError = ValidateRequest();
            if (authError != null) return Unauthorized(new { message = authError });
            var todos = await _context.TodoItems
                .Where(t => t.isEnabled)
                .ToListAsync();
            return Ok(todos);
        }

        // GET: api/todo/{id}
        [HttpGet("{id}")]
        public async Task<ActionResult<TodoItem>> GetTodo(int id)
        {
            var authError = ValidateRequest();
            if (authError != null) return Unauthorized(new { message = authError });
            var todo = await _context.TodoItems.FindAsync(id);
            if (todo == null || !todo.isEnabled) return NotFound();
            return Ok(todo);
        }

        // POST: api/todo
        [HttpPost]
        public async Task<ActionResult<TodoItem>> CreateTodo([FromBody] UpdateTodoDto todoDto)
        {
            var authError = ValidateRequest();
            if (authError != null) return Unauthorized(new { message = authError });
            if (string.IsNullOrEmpty(todoDto.description)) return BadRequest("Description is required.");
            var todo = new TodoItem
            {
                description = todoDto.description,
                completed = todoDto.completed,
                isEnabled = todoDto.isEnabled
            };

            _context.TodoItems.Add(todo);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetTodo), new { id = todo.idTodo }, todo);
        }

        // POST: api/todo/login
        [HttpPost("login")]
        public async Task<ActionResult<TodoItem>> Login([FromBody] LoginDto loginDto)
        {
            var loginItem = new LoginItem
            {
                username = loginDto.username,
                password = loginDto.password,
                isEnabled = loginDto.isEnabled
            };
            Console.WriteLine($"Login attempt for username: {loginItem.username}"); // Log del tentativo di login
            if (loginItem == null) return NotFound();

            var user = await _context.LoginItems.FirstOrDefaultAsync(u => u.username == loginDto.username);

            if (user == null || user.password != loginDto.password) 
            {
                // Se l'utente non esiste o la password non corrisponde
                return Unauthorized("Email o Password errate!");
            }

            var token = GenerateJwtToken(user);
            ValidTokens.Add(token); // Salva il token nella whitelist
            
            Console.WriteLine($"Generated JWT Token: {token}"); // Log del token generato
            return Ok(new { token });
        }

        // GET: api/todo/verify
        [HttpGet("verify")]
        public IActionResult VerifyToken()
        {
            var authHeader = Request.Headers["Authorization"].ToString();
            
            if (string.IsNullOrEmpty(authHeader))
                return Unauthorized(new { message = "Token mancante" });

            var token = authHeader.Replace("Bearer ", "");

            if (!ValidTokens.Contains(token))
                return Unauthorized(new { message = "Token non valido o non autenticato" });

            var tokenHandler = new JwtSecurityTokenHandler();
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(GetRequiredSecretKey()));

            try
            {
                tokenHandler.ValidateToken(token, new TokenValidationParameters
                {
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = securityKey,
                    ValidateIssuer = false,
                    ValidateAudience = false,
                    ClockSkew = TimeSpan.Zero
                }, out SecurityToken validatedToken);

                return Ok(new { message = "Token valido", authenticated = true });
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Token validation error: {ex.Message}");
                return Unauthorized(new { message = "Token non valido", error = ex.Message });
            }
        }

        private string GenerateJwtToken(LoginItem user)
        {   
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(GetRequiredSecretKey()));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);
            
            var claims = new[] 
            {
                new Claim(ClaimTypes.NameIdentifier, user.idUser.ToString()),
                new Claim(ClaimTypes.Email, user.username)
            };

            var token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddHours(1), // Il token scade dopo 1 ora
                signingCredentials: credentials);

            var tokenHandler = new JwtSecurityTokenHandler();
            return tokenHandler.WriteToken(token);
        }

        // PUT: api/todo/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateTodo(int id, [FromBody] UpdateTodoDto updatedTodo)
        {
            var authError = ValidateRequest();
            if (authError != null) return Unauthorized(new { message = authError });
            var todo = await _context.TodoItems.FindAsync(id);
            if (todo == null || !todo.isEnabled) return NotFound();
            todo.description = updatedTodo.description;
            todo.completed = updatedTodo.completed;
            todo.isEnabled = updatedTodo.isEnabled;

            await _context.SaveChangesAsync();
            return NoContent();
        }

        // DELETE: api/todo/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteTodo(int id)
        {
            var authError = ValidateRequest();
            if (authError != null) return Unauthorized(new { message = authError });
            var todo = await _context.TodoItems.FindAsync(id);
            if (todo == null || !todo.isEnabled) return NotFound();
            todo.isEnabled = false;
            await _context.SaveChangesAsync();

            return NoContent();
        }

        // Helper: valida il JWT da Authorization header
        private string? ValidateRequest()
        {
            var authHeader = Request.Headers["Authorization"].ToString();
            if (string.IsNullOrEmpty(authHeader))
                return "Token mancante";
            var token = authHeader.Replace("Bearer ", "");
            if (!ValidTokens.Contains(token))
                return "Token non autenticato";
            var tokenHandler = new JwtSecurityTokenHandler();
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(GetRequiredSecretKey()));
            try
            {
                tokenHandler.ValidateToken(token, new TokenValidationParameters
                {
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = securityKey,
                    ValidateIssuer = false,
                    ValidateAudience = false,
                    ClockSkew = TimeSpan.Zero
                }, out _);
                return null;
            }
            catch
            {
                return "Token non valido";
            }
        }
        private string GetRequiredSecretKey()
        {
            return _config["SecretKey:Key"]
                ?? throw new InvalidOperationException("Missing configuration value: SecretKey:Key");
        }

/*         [HttpGet("protected-resource")]
        public IActionResult GetProtectedResource()
        {
            var token = Request.Headers["Authorization"].ToString().Replace("Bearer ", "");

            if (string.IsNullOrEmpty(token))
                return Unauthorized("Token mancante");

            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes("super-secret-key");

            try
            {
                var claimsPrincipal = tokenHandler.ValidateToken(token, new TokenValidationParameters
                {
                    ValidateIssuer = false,
                    ValidateAudience = false,
                    ValidateLifetime = true, // Verifica la scadenza
                    IssuerSigningKey = new SymmetricSecurityKey(key)
                }, out SecurityToken validatedToken);

                // A questo punto, il token è valido e puoi accedere ai claim
                var userId = claimsPrincipal.FindFirst(ClaimTypes.NameIdentifier).Value;
                
                // Ritorna la risorsa protetta
                return Ok(new { message = "Risorsa protetta accessibile!" });
            }
            catch (Exception)
            {
                return Unauthorized("Token non valido o scaduto");
            }
        } */

    }
}