package learningVertX.eventBusCommunication.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor   // jackson
@NoArgsConstructor    // jacktion
public class PING
{
  private String  messages;
  private boolean enabled;
}
