# JavaFX Login App

A JavaFX application that demonstrates a simple login flow with scene switching, user registration, and a post-login screen.

## 📋 Features
- JavaFX-based GUI with multiple scenes
- Login functionality handled by `Login.java`
- User registration form via `NewUser.java`
- Post-login view handled by `AfterLogin.java`
- Scene switching via `Main.changeScene()`

## 📂 Project Structure
"# LoginSurface" 

## 🚀 How to Run
1. Make sure **Java 17+** and **JavaFX SDK** are installed.
2. Import the project into your IDE (e.g., IntelliJ IDEA).
3. Configure JavaFX libraries in the project settings.
4. Run `Main.java`.

### From the Command Line
```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml org/example/loginsurface/*.java
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml org.example.loginsurface.Main

