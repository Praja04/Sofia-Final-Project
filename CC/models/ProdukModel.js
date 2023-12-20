import User from "./UserModel.js"; // Pastikan path-nya sesuai dengan struktur folder proyek
import { Sequelize } from "sequelize";
import db from "../config/Database.js";

const { DataTypes } = Sequelize;

const Produk = db.define('items', {
    item_id: {
        type: DataTypes.INTEGER,
        autoIncrement: true,
        primaryKey: true,
        allowNull: false,
    },
    user_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
    },
    nama_barang: {
        type: DataTypes.STRING,
        allowNull: false,
        validate: {
            notEmpty: true,
            len: [3, 255]
        }
    },
    jumlah: {
        type: DataTypes.INTEGER,
        allowNull: false,
        validate: {
            notEmpty: true,
        }
    },
    harga: {
        type: DataTypes.DECIMAL(10, 2),
        allowNull: false,
        validate: {
            notEmpty: true
        }
    }
}, {
    freezeTableName: true,
    timestamps: true // Menambahkan createdAt dan updatedAt
});


// Membuat foreign key secara terpisah (jika tidak berhasil sebelumnya)
Produk.belongsTo(User, { foreignKey: 'user_id' });
User.hasMany(Produk, { foreignKey: 'user_id' });

export default Produk;
