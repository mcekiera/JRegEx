package Model.Constructs.Types;

import Model.Constructs.Types.Alternation.Alternation;
import Model.Constructs.Types.Quantifiable.Quantifiable;

public interface Reversible {

    void absorbLast(Quantifiable construct);
    void absorbAll(Alternation construct);

}
