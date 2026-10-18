using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class UpdateTodoDto
{
    [Column("description")]
    [Required]
    public required string description { get; set; }
    [Column("completed")]
    [Required]
    public required bool completed { get; set; }
    [Column("isEnabled")]
    [Required]
    public required bool isEnabled { get; set; }
}