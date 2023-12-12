import User from "../models/UserModel.js";
import argon2 from "argon2";

export const Register = async(req, res) =>{
    const {nama, email, password, confPassword} = req.body;
    if(password !== confPassword) return res.status(400).json({response: "Password dan Confirm Password Tidak Cocok"});
    const hashPassword = await argon2.hash(password);
    try {
        await User.create({
            nama: nama,
            email: email,
            password: hashPassword,
        });
        res.status(201).json({response: "Register Berhasil"});
    } catch (error) {
        res.status(400).json({response: error.message});
    }
}
// Login
export const Login = async (req, res) =>{
    const user = await User.findOne({
        where: {
            email: req.body.email
        }
    });
    // Verifikasi user
    if(!user) return res.status(404).json({response: "User Tidak Ditemukan"});
    const match = await argon2.verify(user.password, req.body.password);
    if(!match) return res.status(400).json({response: "Password Salah"});
    req.session.userId = user.id;
    const id = user.id;
    const nama = user.nama;
    const email = user.email;
    res.status(200).json({
        response: "Berhasil Login",
        data: {
            id: id,
            nama: nama,
            email: email
        }
      });
} 
// Melihat data sendiri
export const Me = async (req, res) => {
    if(!req.session.userId){
        return res.status(401).json({response: "Mohon login dahulu"});
    }
    const user = await User.findOne({
        attributes:['id','nama','email'],
        where: {
            id: req.session.userId
        }
    });
    if(!user) return res.status(404).json({response: "User Tidak Ditemukan"})
    res.status(200).json(user);
}
// Logout
export const Logout = (req, res) => {
    if(!req.session.userId){
        return res.status(401).json({response: "Mohon login dahulu"});
    }
    req.session.destroy((err)=>{
        if(err) return res.status(400).json({response: "Tidak dapat logout"});
        res.status(200).json({response: "Anda berhasil logout"});
    })
}