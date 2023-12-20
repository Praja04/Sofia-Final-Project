import User from "../models/UserModel.js";
import argon2 from "argon2";
import Saldo from "../models/SaldoModel.js";

export const Register = async (req, res) => {
    const { nama, email, password, confPassword } = req.body;
    if (password !== confPassword) return res.status(400).json({ response: "Password dan Confirm Password Tidak Cocok" });
    const hashPassword = await argon2.hash(password);
    try {
        await User.create({
            nama: nama,
            email: email,
            password: hashPassword,
        });
        res.status(201).json({ response: "Register Berhasil" });
    } catch (error) {
        res.status(400).json({ response: error.message });
    }
}

// Login
export const Login = async (req, res) => {
    const user = await User.findOne({
        where: {
            email: req.body.email
        }
    });
    // Verifikasi user
    if (!user) return res.status(404).json({ response: "User Tidak Ditemukan" });
    const match = await argon2.verify(user.password, req.body.password);
    if (!match) return res.status(400).json({ response: "Password Salah" });
    req.session.userId = user.user_id;
    const user_id = user.user_id;
    const nama = user.nama;
    const email = user.email;
    res.status(200).json({
        response: "Berhasil Login",
        data: {
            user_id: user_id,
            nama: nama,
            email: email
        }
    });
}

// Melihat data sendiri
export const Me = async (req, res) => {
    if (!req.session.userId) {
        return res.status(401).json({ response: "Mohon login dahulu" });
    }
    const user = await User.findOne({
        attributes: ['user_id', 'nama', 'email'],
        where: {
            user_id: req.session.userId
        }
    });
    if (!user) return res.status(404).json({ response: "User Tidak Ditemukan" });
    res.status(200).json(user);
}

// Logout
export const Logout = (req, res) => {
    if (!req.session.userId) {
        return res.status(401).json({ response: "Mohon login dahulu" });
    }
    req.session.destroy((err) => {
        if (err) return res.status(400).json({ response: "Tidak dapat logout" });
        res.status(200).json({ response: "Anda berhasil logout" });
    });
}


//ambil saldo
export const withdrawSaldo = async (req, res) => {
    const { jumlahPenarikan } = req.body;
    const userId = req.session.userId;

    try {
        // Pastikan userId ditemukan
        if (!userId) {
            return res.status(404).json({ msg: "Pengguna tidak ditemukan" });
        }

        // Ambil informasi pengguna
        const user = await User.findByPk(userId);

        // Pastikan pengguna ditemukan
        if (!user) {
            return res.status(404).json({ msg: "Pengguna tidak ditemukan" });
        }

        // Pastikan saldo cukup
        if (user.saldo < jumlahPenarikan) {
            return res.status(400).json({ msg: "Saldo tidak mencukupi" });
        }

        // Catat transaksi saldo keluar
        await Saldo.create({
            userId: userId,
            totalSaldo: jumlahPenarikan,
            jenisSaldo: 'keluar',
        });

        // Update saldo pada tabel User (kurangi saldo keluar)
        if(user.saldo> jumlahPenarikan){user.saldo -= jumlahPenarikan;
        await user.save();
        }
        res.status(200).json({ msg: "Penarikan Berhasil" });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
};


export const getSaldo = async (req, res) => {
    const user_id = req.session.userId;
    try {
        const userSaldo = await Saldo.findAll({
            where: {
                userId: user_id
            }
        });

        if (!userSaldo) {
            return res.status(404).json({ msg: "Saldo not found" });
        }

        const saldoTotal = userSaldo.reduce((total, saldo) => {
            return saldo.jenisSaldo === 'masuk' ? total + saldo.totalSaldo : total - saldo.totalSaldo;
        }, 0);

        res.status(200).json({ saldo: saldoTotal });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
};