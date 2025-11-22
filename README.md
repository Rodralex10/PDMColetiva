# MyChat — chat global com Firebase Firestore

App Android (Java + AndroidX) simples e direto: login/registro com Firebase Auth e chat em tempo real via Cloud Firestore. Visual limpo com Material Components.

## Destaques
- Auth email/senha com displayName definido no registro (ou prefixo do email se faltar).
- Chat global unico: colecao `messages` com `text`, `senderName`, `timestamp` (server timestamp).
- Atualizacao em tempo real com `addSnapshotListener`.
- Botao de logout na tela principal.
- Min SDK 26, target/compile 34, Java 17, AndroidX + Material.

## Stack
- Linguagem: Java
- UI: AndroidX, Material Components
- Backend: Firebase Auth, Firebase Cloud Firestore (via Firebase BoM)

## Configuracao rapida
1. No Firebase Console, crie um app Android com package `com.example.mychat`.
2. Baixe `google-services.json` e coloque em `app/google-services.json`.
3. Ative Auth por email/senha e crie um banco Firestore em modo de teste (regras abertas so no dev).
4. Emulador/dispositivo precisa de Google Play Services atualizado.
5. Sincronize o Gradle no Android Studio.

## Build & Run
- Android Studio: Build > Make Project e depois Run.
- CLI: `./gradlew assembleDebug` ou `./gradlew installDebug`.

## Mapa do projeto
- `app/src/main/java/com/example/mychat/LoginActivity.java` — login/registro e definicao do displayName.
- `app/src/main/java/com/example/mychat/MainActivity.java` — chat, envio, listener Firestore, logout.
- `app/src/main/java/com/example/mychat/MessageAdapter.java` — RecyclerView adapter.
- `app/src/main/java/com/example/mychat/Message.java` — modelo da mensagem.
- Layouts: `res/layout/activity_login.xml`, `activity_main.xml`, `item_message.xml`.
- Gradle: `app/build.gradle.kts` usa Firebase BoM + Auth + Firestore.

## Regras de Firestore (apenas dev)
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```
Antes de producao, publique regras seguras conforme sua politica.

## Notas
- O nome exibido vem do displayName do Firebase Auth (ou prefixo do email/UID se ausente).
- Se algo falhar, confirme Play Services atualizado e se `google-services.json` corresponde ao package `com.example.mychat`.
