// models/SaldoModel.js

import { Sequelize, DataTypes } from "sequelize";
import db from "../config/Database.js";

const Saldo = db.define('saldo', {
    id: {
        type: DataTypes.INTEGER,
        autoIncrement: true,
        primaryKey: true,
        allowNull: false,
    },
    userId: {
        type: DataTypes.INTEGER,
        allowNull: false,
    },
    totalSaldo: {
        type: DataTypes.DOUBLE,
        allowNull: false,
    },
    jenisSaldo: { // Ganti nama kolom menjadi jenisSaldo
        type: DataTypes.ENUM('masuk', 'keluar'),
        allowNull: false,
    },
}, {
    freezeTableName: true
});

export default Saldo;
