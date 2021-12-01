import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlusSquare, faMinusSquare, faEdit, faTimes} from '@fortawesome/free-solid-svg-icons'
import { useContext, useEffect, useState } from 'react'
import { Button, Modal } from "react-bootstrap";
import axios from 'axios';
import Cookies from 'js-cookie';
import { Context } from './Global';


export default function Market({uid, name, imageMarket, update})
{
    const [edit, setEdit] = useState(false);
    const [show, setShow] = useState(false);

    const [states, dispatch] = useContext(Context);
    

    const handleClose = () => setShow(false);
    const handleShow = () => {
        setShow(true);
    }
    var cookie = Cookies.get("auth");

    const instance = axios.create({
        baseURL: 'http://localhost:8080/api',
        timeout: 1000,
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin' : "http://localhost:3000/", 
            'Access-Control-Allow-Credentials' : true,
            'Authorization' : "Bearer " + cookie
        }
      })

    async function UpdateProduct(image, name)
    {
        var data = new Object();
        data.iconName = image;
        data.name = name;

        var dataJson = JSON.stringify(data);
        var Endpoint = "market/"+uid;
        await instance.put(Endpoint, dataJson).then(res => {
           setEdit(false);
           update(true);
        }).catch(function (error){
        })
    }

    const SaveEditing = (e) =>{
        e.preventDefault();

        console.log(e);

        UpdateProduct(e.target[0].value, e.target[1].value)
    }

    const Delete = async () =>{
        var Endpoint = "market/"+uid;
        await instance.delete(Endpoint).then(() =>
        {
            update(true);
            handleClose();
        });
    }
    return(
        <div className="card d-flex align-items-center" style={{width: "18rem"}}>
        {!edit &&  
            <div className="card-body text-center">
            <img className="w-20 card-img-top" src={"public/".concat(imageMarket)} alt="Product"/>
            <h5 className="card-title text-center">Pavadinimas:{name}</h5>
            </div>
        }
        {edit && 
            <form onSubmit={SaveEditing}>
            <div className="card-body">
            <h5 className="card-title text-center">Paveiksliuko_Pavadinimas:<input type="text" defaultValue={imageMarket}></input></h5>
            <h5 className="card-title text-center">Pavadinimas:<input type="text" defaultValue={name}></input></h5>
            <h5 className="text-center">
            <Button variant="primary" type="submit">Patvirtinti</Button>
            <FontAwesomeIcon icon={faTimes} style={{width: "30px", height : "30px", verticalAlign : 'middle'}} onClick={() => setEdit(false)}/></h5>
           </div>
           </form>
        }
        {states.userinfo !== null && states.userinfo.admin && !edit && 
        <div className="card-body">
          <FontAwesomeIcon icon={faEdit} style={{width: "30px", height : "30px"}} onClick={() => setEdit(true)}/>
          <FontAwesomeIcon icon={faMinusSquare} style={{width: "30px", height : "30px"}} onClick={handleShow}/>
        <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
            <Modal.Title>Prekės ištrinimas</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <h2>Tikrai norite ištrinti?</h2>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
            Uždaryti
            </Button>
            <Button variant="primary" onClick={Delete}>
            Ištrinti
            </Button>
        </Modal.Footer>
        </Modal>
         </div>
        }
      </div>
    )
}