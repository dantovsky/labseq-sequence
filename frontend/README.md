# Labseq Sequence » Frontend Angular

![alt text](screenshot-frontend.png)

## Frontend

Angular v19.2.0

Install dependences
```shell script
cd frontend
npm install
```

Run the app
```shell script
npm run start
```

In browser:
```
http://localhost:4200
```

### Folder Structure

```
src/
├── app/
│   ├── labseq/                 # Feature component
│   │   ├── labseq.component.ts
│   │   ├── labseq.component.html
│   │   ├── labseq.component.css
│   │   └── labseq.service.ts   # Service with HTTP requisitions
│   │
│   ├── app.component.ts        # Root component
│   └── app.config.ts
│
├── index.html
├── main.ts
└── styles.scss
```
