using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class LoginDto
{
    [Column("username")]
    [Required]
    public required string username { get; set; }
    [Column("password")]
    [Required]
    public required string password { get; set; }
    [Column("isEnabled")]
    [Required]
    public required bool isEnabled { get; set; }

}