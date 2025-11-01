package Domain.Factories;

import Domain.Entities.Presentation;

public class PresentationFactory {

    /**
     * Maak een presentatie aan
     */
    public static Presentation createPresentation()
    {
        return new Presentation();
    }
}
