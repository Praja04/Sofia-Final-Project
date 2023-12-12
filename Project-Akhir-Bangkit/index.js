import express from "express";
import cors from "cors";
import session from "express-session";
import dotenv from "dotenv";
import db from "./config/Database.js";
import SequelizeStore from "connect-session-sequelize";
import AuthRoute from "./routes/AuthRoute.js";
import ProdukRoute from "./routes/ProdukRoute.js";
// import jwt = require('jsonwebtoken'); 

const app = express(); 

// Generate Table
 (async ()=>{
     await db.sync();
 })()

const sessionStore = SequelizeStore(session.Store);

const store = new sessionStore({
    db: db
})
// Menyimpan KEY 
dotenv.config(); 

const PORT = process.env.PORT || 5000; 

app.use(session({
    secret: process.env.SESS_SECRET,
    resave: false,
    saveUninitialized: true,
    store: store,
    cookie: {
        secure: 'auto'
    }
}));

app.use(cors({
    credentials: true, //Mengirim cookies dan session dari frontend
    origin: 'http://localhost:3000' //Domain untuk mengakses API
}));
app.use(express.json());
app.use(AuthRoute);
app.use(ProdukRoute);

// Test API
app.get("/", (req, res) => {
    return res.send("Successfully Verified"); 
})

// store.sync();

app.listen(PORT, ()=>{
    console.log('Server Running, Status OK');
});