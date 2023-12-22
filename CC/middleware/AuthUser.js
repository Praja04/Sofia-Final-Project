import User from "../models/UserModel.js";
import jwt from 'jsonwebtoken';
export const verifyUser = async (req, res, next) => {
    const token = req.headers.authorization?.split(' ')[1];

    if (!token) {
        return res.status(401).json({ msg: "Token not provided" });
    }

    try {
        const decodedToken = jwt.verify(token, process.env.JWT_SECRET);
        const user = await User.findByPk(decodedToken.userId);

        if (!user) {
            return res.status(404).json({ msg: "User not found" });
        }

        req.userId = user.user_id;
        next();
    } catch (error) {
        res.status(401).json({ msg: "Invalid token" });
    }
};