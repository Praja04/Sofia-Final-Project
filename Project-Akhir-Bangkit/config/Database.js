import { Sequelize } from "sequelize";

const db = new Sequelize('projectakhir', 'root', '', {
    host: "localhost",
    dialect: "mysql"
});

export default db;