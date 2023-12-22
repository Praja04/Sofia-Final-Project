import jwt from 'jsonwebtoken';
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
    const { email, password } = req.body;

    try {
        const user = await User.findOne({
            where: {
                email: email
            }
        });

        if (!user) {
            return res.status(404).json({ msg: "Email atau password salah" });
        }

        const validPassword = await argon2.verify(user.password, password);

        if (!validPassword) {
            return res.status(400).json({ msg: "Email atau password salah" });
        }

        // Create and sign a token
        const token = jwt.sign({ userId: user.user_id, email: user.email }, process.env.JWT_SECRET, { expiresIn: '1h' });

        res.status(200).json({ token, msg: "Login Successful" });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
};

// Melihat data sendiri
export const Me = async (req, res) => {
    try {
        // Extract token from header
        const token = req.headers.authorization.split(' ')[1];

        // Verify token
        const decodedToken = jwt.verify(token, process.env.JWT_SECRET);

        // Get user information from token
        const user = await User.findByPk(decodedToken.userId);

        if (!user) {
            return res.status(404).json({ response: "User not found" });
        }

        res.status(200).json(user);
    } catch (error) {
        res.status(401).json({ response: "Invalid token" });
    }
}

// Logout Endpoint
export const Logout = (req, res) => {
    try {
        // Extract token from header
        const token = req.headers.authorization.split(' ')[1];

        // Destroy session on the server
        req.session.destroy((err) => {
            if (err) return res.status(400).json({ response: "Unable to logout" });

            // Log user out by sending a new token with a short expiration time
            const expiredToken = jwt.sign({}, process.env.JWT_SECRET, { expiresIn: '1s' });

            // Send the expired token to the client, forcing it to logout
            res.status(200).json({ token: expiredToken, response: "Logout successful" });
        });
    } catch (error) {
        res.status(500).json({ response: error.message });
    }
};



//ambil saldo
export const withdrawSaldo = async (req, res) => {

     // Mendapatkan token dari header Authorization
     const token = req.headers.authorization.split(' ')[1];

     // Melakukan verifikasi token
     const decodedToken = jwt.verify(token, process.env.JWT_SECRET);
 
    const { jumlahPenarikan } = req.body;
    const userId = decodedToken.userId;

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
     // Mendapatkan token dari header Authorization
     const token = req.headers.authorization.split(' ')[1];

     // Melakukan verifikasi token
     const decodedToken = jwt.verify(token, process.env.JWT_SECRET);
 
    const user_id = decodedToken.userId;
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