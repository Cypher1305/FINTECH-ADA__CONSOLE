package ci.ada.Interfaces;


import ci.ada.facade.MainContext;

public interface AppState {
    void handle(MainContext context);
}
