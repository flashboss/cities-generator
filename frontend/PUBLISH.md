# Come pubblicare cities-generator-frontend su npm

## Prerequisiti

1. Avere un account npm (creare su https://www.npmjs.com/signup)
2. Essere autenticati con npm:
   ```bash
   npm login
   ```

## Passi per la pubblicazione

### 1. Verificare la versione

Controlla che la versione in `package.json` sia corretta e incrementata rispetto all'ultima versione pubblicata.

### 2. Build del progetto

```bash
npm run build
```

Questo genererà tutti i bundle necessari nella cartella `dist/`.

### 3. Test locale (opzionale)

Puoi testare il pacchetto localmente prima di pubblicarlo:

```bash
npm pack
```

Questo creerà un file `.tgz` che puoi installare in un altro progetto per testare.

### 4. Dry-run (consigliato)

Prima di pubblicare, fai un dry-run per vedere cosa verrà pubblicato:

```bash
npm run publish:dry-run
```

### 5. Pubblicazione

Quando sei pronto, pubblica su npm:

```bash
npm run publish:npm
```

Oppure direttamente:

```bash
npm publish --access public
```

Il flag `--access public` è necessario per pacchetti con scope (se il nome del pacchetto inizia con `@`), ma è comunque una buona pratica.

## Aggiornare la versione

Per aggiornare la versione, modifica il campo `version` in `package.json` seguendo il [Semantic Versioning](https://semver.org/):

- **MAJOR** (1.0.0 → 2.0.0): Cambiamenti incompatibili
- **MINOR** (1.0.0 → 1.1.0): Nuove funzionalità retrocompatibili
- **PATCH** (1.0.0 → 1.0.1): Bug fix retrocompatibili

Puoi anche usare npm version:

```bash
npm version patch  # 1.2.6 → 1.2.7
npm version minor  # 1.2.6 → 1.3.0
npm version major  # 1.2.6 → 2.0.0
```

Questo comando aggiorna automaticamente `package.json` e crea un commit git.

## Verifica dopo la pubblicazione

Dopo la pubblicazione, verifica che il pacchetto sia disponibile:

```bash
npm view cities-generator-frontend
```

O visita: https://www.npmjs.com/package/cities-generator-frontend

## Installazione del pacchetto pubblicato

Gli utenti potranno installare il pacchetto con:

```bash
npm install cities-generator-frontend
```

## Note

- Il comando `prepublishOnly` in `package.json` esegue automaticamente il build prima della pubblicazione
- I file pubblicati sono definiti nel campo `files` di `package.json`: `dist`, `src`, e `README.md`
- I file esclusi sono definiti in `.npmignore`

