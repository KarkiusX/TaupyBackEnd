import React, { useContext } from "react";
import { Col, Container, Row } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { ErrorMessage } from '@hookform/error-message';
import axios from "axios";
import { useNavigate } from "react-router-dom";
import {Context} from "./Global"
import Cookies from "js-cookie";

export default function Login(){
    const [state, dispatch] = useContext(Context);

    let navigate = useNavigate();

    const {setError, register, getValues, handleSubmit, formState : {errors}} = useForm({
        mode: 'onBlur'
      })
    const instance = axios.create({
    baseURL: 'https://taupyk.herokuapp.com/',
    timeout: 1000,
    withCredentials: true
    })
    const onSubmit = async (data) => {
        await instance.post('login/',data).then(() => {
            dispatch({type: 'Active'})
            navigate("/")
        }).catch(function (error){
           if(error.response.status === 401)
           {
                setError('email', {
                type: "server",
                message: 'Vartotojas neegzistuojas arba netaisyklingas slaptažodis',
              });
           }
        });
    

       // navigate("/");
    }
    const SubmitError = (errors) =>{

    }
    return (
        <Container>
            <Row className="justify-content-sm-center pt-5">
            <Col xs lg="3">
                <form onSubmit={handleSubmit(onSubmit, SubmitError)}>
                    <h3>Prisijungimo langas</h3>
                    <div className="form-group">
                        <label>Paštas</label>
                        <input {...register("email", {required: 'Įrašykite savo el.paštą'})} type="email" className="form-control" placeholder="Paštas" />
                        <ErrorMessage errors={errors} name="email" render={({ message }) => <p className="text-danger">{message}</p>} />
                    </div>
                    <div className="form-group mt-2">
                        <label>Slaptažodis</label>
                        <input {...register("password", {required: 'Įrašykite slaptažodį'})} type="password" className="form-control" placeholder="Slaptažodis" />
                        <ErrorMessage errors={errors} name="password" render={({ message }) => <p className="text-danger">{message}</p>} />
                    </div>
                    <div className="form-group">
                        <div className="custom-control custom-checkbox mt-2">
                            <input {...register("stayLoggedIn")} type="checkbox" className="custom-control-input" id="customCheck1" />
                            <label className="custom-control-label" htmlFor="customCheck1">Prisiminti mane</label>
                        </div>
                    </div>

                    <button type="submit" className="btn btn-primary btn-block mt-2">Prisijungti</button>
                </form>
                </Col>
        </Row>
        </Container>
    );
}