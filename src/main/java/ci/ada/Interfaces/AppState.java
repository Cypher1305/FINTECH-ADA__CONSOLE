package ci.ada.Interfaces;


import ci.ada.state.MainContext;

public interface AppState {
    void handle(MainContext context);
}
