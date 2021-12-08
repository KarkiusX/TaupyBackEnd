import Market from "./Market";

import './App.css';
import { Component, useContext, useState, useEffect} from 'react';
import { Context } from './Global';
import Register from './Register';
import { useForm } from 'react-hook-form';
import { Modal, Row, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router';
import Product from './Product';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faPlusSquare} from '@fortawesome/free-solid-svg-icons'
import { ErrorMessage } from '@hookform/error-message';
import Cookies from 'js-cookie';
import axios from "axios";
import { Actions } from "./const/Actions";



export default function Markets()
{
    const [state, dispatch] = useContext(Context);
    const {register,getValues, handleSubmit, formState : {errors}} = useForm()
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => {
        setShow(true);
    }

    const [updateList, setUpdate] = useState(false);

    const onSubmitCreate = async (data) =>{
        var cookie = Cookies.get("auth");
    
        const SubmitInstance = axios.create({
          baseURL: 'https://taupyk.herokuapp.com/',
          timeout: 1000,
          headers: {
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin' : "http://localhost:3000/", 
              'Access-Control-Allow-Credentials' : true,
              'Authorization' : "Bearer " + cookie
          }
        })
    
        await SubmitInstance.post('/market/', data).then(res => {
          handleClose();
          setUpdate(Actions.PREPOST);
          setUpdate(Actions.POSTPOST);
        });
      }
      useEffect(()=> {
        async function fetchData() {
            const instance = axios.create({
                baseURL: 'http://localhost:8080/api',
                timeout: 1000
              })
            let result = await instance.get('market');
            dispatch({type: 'Markets', data: result.data});
    
        }
        fetchData();
      },[updateList]);

    return(
<div className="h-75">
      <div className="container h-75">
        <Row className="d-flex d-infline-flex">
        {state.userinfo != null && state.userinfo.admin && <FontAwesomeIcon icon={faPlusSquare} style={{width: "100px", height : "100px"}} onClick={handleShow}/>}
              {Array.isArray(state.markets) && state.markets.length >=1 ? state.markets.map((market) => (
                        <Market
                        uid={`${market.uid}`}
                        name={`${market.name}`}
                        imageMarket={`${market.iconName}`}
                        update = {setUpdate}
                        />
                )) : 
                     <h3 className="text-center">Produktų nerasta</h3>
                 }
        </Row>
        <Modal show={show} onHide={handleClose}>
            <Modal.Body>
            <form onSubmit={handleSubmit(onSubmitCreate)}>
                <h3>Parduotuvės kūrimas</h3>
                <div className="form-group mt-2">
                    <label>Parduotuvės pavadinimas</label>
                    <input {...register("name", {required: "Įveskite parduotuvės pavadinimą"})} type="name" className="form-control" placeholder="Pavadinimas"/>
                    <ErrorMessage errors={errors} name="name" render={({ message }) => <p className="text-danger">{message}</p>} />
                </div>
                <div className="form-group mt-2">
                    <label>Parduotuvės nuotrauką</label>
                    <input {...register("iconName", {required: 'Įrašykite nuotraukos pavadinimą'})} type="name" className="form-control" placeholder="Nuotraukos pavadinimas" />
                    <ErrorMessage errors={errors} name="iconName" render={({ message }) => <p className="text-danger">{message}</p>} />
                </div>
                <button type="submit" className="btn btn-primary btn-block mt-4">Sukurti</button>
            </form>
            </Modal.Body>
             <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                Uždaryti
                </Button>
             </Modal.Footer>
        </Modal>
     </div>
     </div>
    )
}