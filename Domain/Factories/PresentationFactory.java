package Domain.Factories;

import Domain.Entities.Presentation;

//Factory pattern correct? Definitely not abstract factory : Tibo
public class PresentationFactory {

    /**
     * Maak een presentatie aan
     */
    public static Presentation createPresentation()
    {
        return new Presentation();
    }
}
