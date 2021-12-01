import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlusSquare, faMinusSquare, faEdit, faTimes} from '@fortawesome/free-solid-svg-icons'
import { useContext, useEffect, useState } from 'react'
import { Button, Modal } from "react-bootstrap";
import axios from 'axios';
import Cookies from 'js-cookie';
import { Context } from './Global';

function Product({uid, name, price, imageMarket, imageProduct, send, update}) {

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

    async function UpdateProduct(name, price)
    {
        var data = new Object();
        data.name = name;
        data.price = parseFloat(price);

        var dataJson = JSON.stringify(data);
        var Endpoint = "product/"+uid;
        await instance.put(Endpoint, dataJson).then(res => {
           setEdit(false);
           update(false);
           update(true);
        }).catch(function (error){
        })
    }

    const SaveEditing = (e) =>{
        e.preventDefault();

        UpdateProduct(e.target[0].value, e.target[1].value)
    }

    const Delete = async () =>{
        var Endpoint = "product/"+uid;
        await instance.delete(Endpoint).then(() =>
        {
            update(true);
            handleClose();
        });
    }

    return(
        <div className="card d-flex align-items-center" style={{width: "18rem"}}>
        <img src={"public/".concat(imageMarket)} width="100 px" height="100px" onClick={() => send({uid, name, price, imageMarket, imageProduct})}></img>
        <img className="w-20 card-img-top" src={"public/".concat(imageProduct)} alt="Product"></img>
        {!edit && 
                <div className="card-body" onClick={() => send({uid, name, price, imageMarket, imageProduct})}>
                <h5 className="card-title text-center">{name}</h5>
                <h5 className="card-title">Price:{price} €</h5>
              </div>}
        {edit && 
            <form onSubmit={SaveEditing}>
            <div className="card-body">
            <h5 className="card-title text-center">Name:<input type="text" defaultValue={name}></input></h5>
            <h5 className="card-title text-center">Price, €:<input type="text" defaultValue={price}></input></h5>
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

export default Product
