import Produk from "../models/ProdukModel.js";
import User from "../models/UserModel.js";
import {Op} from "sequelize";

export const getProducts = async(req, res) =>{
    try {
        let response;
            response = await Produk.findAll({
                attributes:['id','nama','jumlah', 'harga'],
            });
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const getProductsById = async(req, res) =>{
    try {
        const product = await Produk.findOne({
            where:{
                id: req.params.id
            }
        });
        if(!product) return res.status(404).json({msg: "Data Tidak Ditemukan"});
        let response;
        if(req.role === "admin"){
            //relasi user dengan produk
            //jika user admin maka bisa melihat semua product
            response = await Produk.findOne({
                attributes:['id','nama','jumlah','harga'],
                where:{
                    id: product.id
                },
                include:[{
                    model: User,
                    attributes:['nama','email']
                }]
            });
        }else{
            //jika user bukan admin, maka hanya bisa melihat input user tersebut
            response = await Produk.findOne({
                attributes:['id','nama','jumlah','harga'],
                where:{
                    [Op.and]:[{id: product.id}, {userId: req.userId}]
                },
                include:[{
                    model: User,
                    attributes:['nama','email']
                }]
            });
        }
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const createProducts = async(req, res) =>{
    const {nama, jumlah,harga} = req.body;
    try {
        await Produk.create({
            nama: nama,
            jumlah: jumlah,
            harga: harga,
            userId: req.userId
        });
        res.status(201).json({msg: "Product Created Successfully"})
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const updateProducts = async(req, res) =>{
    try {
        const product = await Produk.findOne({
            where:{
                id: req.params.id
            }
        });
        if(!product) return res.status(404).json({msg: "Data Tidak Ditemukan"});
        const {nama, jumlah, harga} = req.body;
        if(product){
            await Produk.update({nama, jumlah, harga}, {
                where:{
                    id: product.id
                }
            });
        }
        
        res.status(200).json({msg: "Product Updated Successfully"});
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}

export const deleteProducts = async(req, res) =>{
    try {
        const product = await Produk.findOne({
            where:{
                id: req.params.id
            }
        });
        if(!product) return res.status(404).json({msg: "Data Tidak Ditemukan"});
        const {nama, jumlah, harga} = req.body;
        if(product){
            await Produk.destroy({
                where:{
                    id: product.id
                }
            });
        }
        res.status(200).json({msg: "Product Deleted Successfully"});
    } catch (error) {
        res.status(500).json({msg: error.message});
    }
}