using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

[Table("users_tb", Schema = "dbo")] 
public class LoginItem
{
    [Key]
    [Column("idUser")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    [Required]
    public int idUser { get; set; }
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