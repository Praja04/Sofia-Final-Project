import Produk from "../models/ProdukModel.js";
import jwt from 'jsonwebtoken';

export const getProducts = async (req, res) => {
  try {
    // Mendapatkan token dari header Authorization
    const token = req.headers.authorization.split(' ')[1];

    // Melakukan verifikasi token
    const decodedToken = jwt.verify(token, process.env.JWT_SECRET);

    // Melakukan operasi yang sesuai dengan token, seperti mengambil produk
    const product = await Produk.findAll({
      where: {
        user_id: decodedToken.userId, // Gunakan decodedToken.userId untuk mendapatkan id pengguna yang saat ini login
      },
    });

    if (!product) return res.status(404).json({ msg: "Data Tidak Ditemukan" });

    let response;
    if (product) {
      // Relasi user dengan produk
      // Jika user admin maka bisa melihat semua product
      response = await Produk.findAll({
        attributes: ['item_id','nama_barang', 'jumlah','harga'],
        where: {
            user_id: decodedToken.userId,
        },
      });
    }

    res.status(200).json(response);
  } catch (error) {
    res.status(500).json({ msg: error.message });
  }
};

export const getStokProducts = async (req, res) => {
    try {
         // Mendapatkan token dari header Authorization
    const token = req.headers.authorization.split(' ')[1];

    // Melakukan verifikasi token
    const decodedToken = jwt.verify(token, process.env.JWT_SECRET);

        const product = await Produk.findOne({
            where: {
                item_id: req.params.id,
                user_id: decodedToken.userId  // Gunakan req.userId untuk mendapatkan id pengguna yang saat ini login
                
            }
        });
        if (!product) return res.status(404).json({ msg: "Data Tidak Ditemukan" });
        let response;
        if (product) {
            //relasi user dengan produk
            //jika user admin maka bisa melihat semua product
            response = await Produk.findOne({
                attributes: ['nama_barang', 'jumlah'],
                where: {
                    item_id: product.item_id 
                }
            });
        }
    
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}


export const getProductsById = async (req, res) => {
    try {
         // Mendapatkan token dari header Authorization
    const token = req.headers.authorization.split(' ')[1];

    // Melakukan verifikasi token
    const decodedToken = jwt.verify(token, process.env.JWT_SECRET);

        const product = await Produk.findOne({
            where: {
                item_id: req.params.id,
                user_id: decodedToken.userId  // Gunakan req.userId untuk mendapatkan id pengguna yang saat ini login
                
            }
        });
        if (!product) return res.status(404).json({ msg: "Data Tidak Ditemukan" });
        let response;
        if (product) {
            //relasi user dengan produk
            //jika user admin maka bisa melihat semua product
            response = await Produk.findOne({
                attributes: ['item_id', 'nama_barang', 'jumlah', 'harga'],
                where: {
                    item_id: product.item_id 
                }
            });
        }
    
        res.status(200).json(response);
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}

export const createProducts = async (req, res) => {
    const { nama_barang, jumlah, harga } = req.body;
     // Mendapatkan token dari header Authorization
     const token = req.headers.authorization.split(' ')[1];

     // Melakukan verifikasi token
     const decodedToken = jwt.verify(token, process.env.JWT_SECRET);
 
    const user_id = decodedToken.userId; // Ambil user_id dari sesi

    try {
        // Periksa apakah produk dengan nama yang sama sudah ada untuk user_id yang sedang masuk
        const existingProduct = await Produk.findOne({
            where: {
                nama_barang: nama_barang,
                user_id: user_id
            }
        });

        if (existingProduct) {
            return res.status(400).json({ msg: "Produk dengan nama tersebut sudah ada" });
        }

        

        // Jika tidak ada, tambahkan produk baru
        await Produk.create({
            nama_barang: nama_barang,
            jumlah: jumlah,
            harga: harga,
            user_id: user_id
        });

        res.status(201).json({ msg: "Product Created Successfully" });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}

export const updateProducts = async (req, res) => {
    try {
         // Mendapatkan token dari header Authorization
    const token = req.headers.authorization.split(' ')[1];

    // Melakukan verifikasi token
    const decodedToken = jwt.verify(token, process.env.JWT_SECRET);

        const product = await Produk.findOne({
            where: {
                item_id: req.params.id,
                user_id: decodedToken.userId 
                
            }
        });
        if (!product) return res.status(404).json({ msg: "Data Tidak Ditemukan" });
        const { nama_barang, jumlah, harga } = req.body;
        if (product) {
            await Produk.update({ nama_barang, jumlah, harga }, {
                where: {
                    item_id: product.item_id
                }
            });
        }

        res.status(200).json({ msg: "Product Updated Successfully" });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}

export const deleteProducts = async (req, res) => {
    try {
         // Mendapatkan token dari header Authorization
    const token = req.headers.authorization.split(' ')[1];

    // Melakukan verifikasi token
    const decodedToken = jwt.verify(token, process.env.JWT_SECRET);

        const product = await Produk.findOne({
            where: {
                item_id: req.params.item_id,
                user_id: decodedToken.userId 
            }
        });
        if (!product) return res.status(404).json({ msg: "Data Tidak Ditemukan" });

        if (product) {
            await Produk.destroy({
                where: {
                    item_id: product.item_id
                }
            });
        }
        res.status(200).json({ msg: "Product Deleted Successfully" });
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
}
