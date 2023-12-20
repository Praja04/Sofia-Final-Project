import Produk from "./ProdukModel.js"; // Pastikan path-nya sesuai dengan struktur folder proyek
import { Sequelize } from "sequelize";
import db from "../config/Database.js";
import User from "./UserModel.js";

const { DataTypes } = Sequelize;

const Transaksi = db.define('transactions', {
    transaksi_id: {
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
    },
    total_harga: {
        type: DataTypes.DECIMAL(10, 2),
        allowNull: false,
        validate: {
            notEmpty: true
        }
    }
}, {
    freezeTableName: true,
    timestamps: true
     // Menambahkan createdAt dan updatedAt
});


// Membuat foreign key secara terpisah (jika tidak berhasil sebelumnya)
Transaksi.belongsTo(User, { foreignKey: 'user_id' });
User.hasMany(Transaksi, { foreignKey: 'user_id' });
Transaksi.belongsTo(Produk, { foreignKey: 'item_id' });
Produk.hasMany(Transaksi, { foreignKey: 'item_id' });
export default Transaksi;
