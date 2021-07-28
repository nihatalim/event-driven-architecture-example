package tr.com.nihatalim.eda.core.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String cardNo;
    private String owner;
    private String expirationDate;
    private String cvv;
}
