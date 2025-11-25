# MyChat — chat global com Firebase Firestore

App Android (Java + AndroidX) simples e direto: login/registo com Firebase Auth e chat em tempo real via Cloud Firestore.

## Destaques
- Auth email/senha com displayName definido no registo (ou prefixo do email se faltar).
- Chat global único: coleção `messages` com `text`, `senderName`, `timestamp` (server timestamp).
- Atualização em tempo real com `addSnapshotListener`.
- Botão de logout na tela principal.

## Stack
- Linguagem: Java
- UI: AndroidX, Material Components
- Backend: Firebase Auth, Firebase Cloud Firestore (via Firebase BoM)

## Build & Run
- Android Studio: Build > Make Project e depois Run.
- CLI: `./gradlew assembleDebug` ou `./gradlew installDebug`.

## Mapa do projeto
- `app/src/main/java/com/example/mychat/LoginActivity.java` — login/registo e definição do displayName.
- `app/src/main/java/com/example/mychat/MainActivity.java` — chat, envio, listener Firestore, logout.
- `app/src/main/java/com/example/mychat/MessageAdapter.java` — RecyclerView adapter.
- `app/src/main/java/com/example/mychat/Message.java` — modelo da mensagem.
- Layouts: `res/layout/activity_login.xml`, `activity_main.xml`, `item_message.xml`.
- Gradle: `app/build.gradle.kts` usa Firebase BoM + Auth + Firestore.

