import Transaksi from "../models/TransaksiModel.js";
import Saldo from "../models/SaldoModel.js";
import Produk from "../models/ProdukModel.js";
import User from "../models/UserModel.js";

export const getTransaksi = async (req, res) => {
    try {
        const transaksi = await Transaksi.findAll({
            where: {
                user_id: req.userId
            }
        });
        res.status(200).json(transaksi);
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}

export const getTransaksiById = async (req, res) => {
    try {
        const transaksi = await Transaksi.findOne({
            where: {
                transaksi_id: req.params.id,
                user_id: req.userId
            }
        });
        if (!transaksi) return res.status(404).json({ msg: "Data Tidak Ditemukan" });
        res.status(200).json(transaksi);
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}

export const createTransaksi = async (req, res) => {
    const { nama_barang, jumlah } = req.body;
    const user_id = req.session.userId; // Ambil user_id dari sesi
    try {
        const existingTransaksi = await Transaksi.findOne({
            where: {
                nama_barang,
                jumlah
            }
        });



        const product = await Produk.findOne({
            where: {
                nama_barang,
                user_id: req.userId
            }
        });

        if (!product) {
            return res.status(404).json({ msg: "Produk tidak ditemukan" });
        }

        if (product.jumlah < jumlah) {
            return res.status(400).json({ msg: "Stok produk tidak mencukupi" });
        }

       

        const total_harga = product.harga * jumlah;

        await Produk.update(
            {
                jumlah: product.jumlah - jumlah
            },
            {
                where: {
                    item_id: product.item_id
                }
            }
        );

        await Transaksi.create({
            user_id: req.userId,
            nama_barang,
            jumlah,
            total_harga,
            item_id: product.item_id
        });
         // Catat transaksi saldo masuk
         await Saldo.create({
            userId: user_id,
            totalSaldo: total_harga,
            jenisSaldo: 'masuk',
        });

        // Update saldo pada tabel User (tambahkan saldo masuk)
        const user = await User.findByPk(user_id);
        user.saldo += total_harga;
        await user.save();

        res.status(201).json({ msg: "Transaksi Created Successfully" });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
};

export const updateTransaksi = async (req, res) => {
    try {
        const transaksi = await Transaksi.findOne({
            where: {
                transaksi_id: req.params.id,
                user_id: req.userId
            }
        });
        if (!transaksi) return res.status(404).json({ msg: "Data Tidak Ditemukan" });

        const { nama_barang, jumlah, total_harga } = req.body;

        await Transaksi.update({ nama_barang, jumlah, total_harga }, {
            where: {
                transaksi_id: transaksi.transaksi_id
            }
        });

        res.status(200).json({ msg: "Transaksi Updated Successfully" });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}

export const deleteTransaksi = async (req, res) => {
    try {
        const transaksi = await Transaksi.findOne({
            where: {
                transaksi_id: req.params.id,
                user_id: req.userId
            }
        });
        if (!transaksi) return res.status(404).json({ msg: "Data Tidak Ditemukan" });

        await Transaksi.destroy({
            where: {
                transaksi_id: transaksi.transaksi_id
            }
        });

        res.status(200).json({ msg: "Transaksi Deleted Successfully" });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}
