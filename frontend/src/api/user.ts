import { RegisterData } from "../models/RegisterData.model";
import { paramsMaker } from "../utils/params";

export const register = async (data: RegisterData) => {
  console.log(JSON.stringify(data));

  await fetch("http://localhost:8080/register" + paramsMaker(data), {
    method: "POST",
  });
};
