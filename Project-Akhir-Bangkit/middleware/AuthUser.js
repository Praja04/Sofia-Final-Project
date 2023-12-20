import User from "../models/UserModel.js";

export const verifyUser = async (req, res, next) =>{
    if(!req.session.userId){
        return res.status(401).json({msg: "Mohon login dahulu"});
    }
    const user = await User.findOne({
        where: {
            user_id: req.session.userId  // Ganti 'id' menjadi 'user_id'
        }
    });
    if(!user) return res.status(404).json({msg: "User Tidak Ditemukan"})
    req.userId = user.user_id;  // Ganti 'id' menjadi 'user_id'
    next();
}