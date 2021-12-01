import Cookies from "js-cookie";
import { useContext, useEffect } from "react";
import { useNavigate } from "react-router";
import { Context } from "./Global";

export default function Logout()
{
    const [states, dispatch] = useContext(Context);

    let navigate = useNavigate();


    useEffect(()=> {
        
        Cookies.remove("auth");

        dispatch({type: 'Logout'})

        navigate("/");

    }, []);

    return null;

}