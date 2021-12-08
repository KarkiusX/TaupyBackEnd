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
import { Actions } from './const/Actions';

const axios = require('axios');

export default function Products() {

  const [state, dispatch] = useContext(Context);

  const [productBySearch, getProduct] = useState([]);

  const [firstSearch, setFirstSearch] = useState(false);

  const [searchfield, setSearch] = useState("");

  const [updateList, setUpdate] = useState(false);

  const {register,getValues, handleSubmit, formState : {errors}} = useForm()
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => {
      setShow(true);
  }

  let navigate = useNavigate();


  const instance = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 1000
  })

  
  useEffect(()=> {
    async function fetchData() {
        await instance.get('product/').then((result) => {
          dispatch({type: 'Items', data: result.data});
          console.log(result.data);
          getProduct(result.data.filter(product => product.name.includes(searchfield)));
        });

    }
    fetchData();
  },[updateList]);

  const onClick = data =>{
    navigate("/item",  { state: data });
  }

  const onSubmit = (e) =>{
    e.preventDefault();
    var findByName = e.target[0].value;
    setSearch(findByName);
    getProduct(state.products.filter(product => product.name.includes(findByName)));
    setFirstSearch(true);
  }

  const onSubmitCreate = async (data) =>{
    data.market = {"uid": data.market}

    var cookie = Cookies.get("auth");

    const SubmitInstance = axios.create({
      baseURL: 'https://taupyk.herokuapp.com/',
      timeout: 1000,
      headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Credentials' : true,
          'Authorization' : "Bearer " + cookie
      }
    })

    await SubmitInstance.post('/product/', data).then(res => {
      handleClose();
      setUpdate(Actions.PREPOST);
      setUpdate(Actions.POSTPOST);
    });
  }

  return(
      <div className="container">
        <Row className="d-flex align-items-center">
        <form onSubmit={onSubmit}>
            <div className="form-group d-flex justify-content-center pt-5">
              <input placeholder="Irašykite produktą"/>
              <input className="btn btn-primary rounded-0" type="submit" value="Ieškoti"/>
              {state.userinfo != null && state.userinfo.admin && <FontAwesomeIcon icon={faPlusSquare} style={{width: "30px", height : "30px"}} onClick={handleShow}/>}
            </div>
        </form>
        </Row>
        <Row className="d-flex d-infline-flex pt-5">
              {Array.isArray(productBySearch) && productBySearch.length >=1 ? productBySearch.map((product) => (
                  <Product key={product.uid}
                    uid={`${product.uid}`}
                    name={`${product.name}`}
                    price={`${product.price}`}
                    imageMarket={`${product.market.iconName}`}
                    imageProduct={`${product.productLink}`}
                    update = {setUpdate}
                    send={onClick}
                  />
                )) : firstSearch === true && 
                     <h3 className="text-center">Produktų nerasta</h3>
                    }
        </Row>
        <Modal show={show} onHide={handleClose}>
            <Modal.Body>
            <form onSubmit={handleSubmit(onSubmitCreate)}>
                <h3>Produkto kūrimas</h3>
                <div className="form-group mt-2">
                    <label>Produkto pavadinimas</label>
                    <input {...register("name", {required: "Įveskite produkto vardą"})} type="name" className="form-control" placeholder="Pavadinimas"/>
                    <ErrorMessage errors={errors} name="name" render={({ message }) => <p className="text-danger">{message}</p>} />
                </div>
                <div className="form-group mt-2">
                    <label>Produkto kainą</label>
                    <input {...register("price", {required: 'Įrašykite kainą'})} step="0.1" type="number" className="form-control" placeholder="Kaina" />
                    <ErrorMessage errors={errors} name="price" render={({ message }) => <p className="text-danger">{message}</p>} />
                </div>
                <div className="form-group mt-2">
                    <label>Nuotrauka</label>
                    <input {...register("productLink", {required: 'Įrašykite nuotraukos pavadinimą'})} type="text" className="form-control" placeholder="Nuotrauka" />
                    <ErrorMessage errors={errors} name="productLink" render={({ message }) => <p className="text-danger">{message}</p>} />
                </div>
                <div className="form-group mt-2">
                    <label htmlFor="Select">Parduotuvė</label>
                    <select {...register("market")} className="form-control" id="Select">
                      {state.markets.map((market) =>(
                      <option value={market.uid}>{market.name}</option>
                      ))}
                    </select>
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
  );
}