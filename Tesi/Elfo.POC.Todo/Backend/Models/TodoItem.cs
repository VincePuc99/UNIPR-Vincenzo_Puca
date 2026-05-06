using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

[Table("todos_tb", Schema = "dbo")] 
public class TodoItem
{
    [Key]
    [Column("idTodo")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    [Required]
    public int idTodo { get; set; }
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