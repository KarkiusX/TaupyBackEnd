import axios from "axios";
import { useEffect, useState } from "react";
import { Row,Col,Container } from "react-bootstrap"
import { useForm} from "react-hook-form";
import { ErrorMessage } from '@hookform/error-message';
import { useNavigate } from "react-router-dom";

function Register()
{
    let navigate = useNavigate();

    const {register,getValues, handleSubmit, formState : {errors}} = useForm({
        mode: 'onBlur'
      })

    const instance = axios.create({
        baseURL: 'http://158.129.21.109:8080/',
        timeout: 1000,
        headers: {'Access-Control-Allow-Origin' : '*'}
        })

    const onSubmit = async (data) => {
        delete data['pakartotas_slaptazodis'];

        const res = await instance.post('register/',data).then(result => result.data);

        navigate("/login");


    }
    return (
    <Container>
    <Row className="justify-content-sm-center pt-5">
    <Col xs lg="3">
        <form onSubmit={handleSubmit(onSubmit)}>
            <h3>Registravimo langas</h3>

            <div className="form-group mt-2">
                <label>Vartotojo vardas</label>
                <input {...register("username", {required:  "Pamiršote įvesti vardą", validate: { nameExist: async (value) => {return await instance.get('names/'+ value).then(result => result.data) === true || 'Vardas egzistuojas'}}})} type="name" className="form-control" placeholder="Vardas"/>
                <ErrorMessage errors={errors} name="username" render={({ message }) => <p className="text-danger">{message}</p>} />
            </div>
            <div className="form-group mt-2">
                <label>Paštas</label>
                <input {...register("email", {required: 'Įrašykite savo el.paštą'})} type="email" className="form-control" placeholder="Paštas" />
                <ErrorMessage errors={errors} name="email" render={({ message }) => <p className="text-danger">{message}</p>} />
            </div>
            <div className="form-group mt-2">
                <label>Slaptažodis</label>
                <input {...register("password", {required: 'Įrašykite slaptažodį'})} type="password" className="form-control" placeholder="Slaptažodis" />
                <ErrorMessage errors={errors} name="password" render={({ message }) => <p className="text-danger">{message}</p>} />
            </div>
            <div className="form-group mt-2">
                <label>Patvirtinti slaptažodi</label>
                <input {...register("pakartotas_slaptazodis", {required:"Pakartokite slaptažodį", validate:{ passwordMismatch: (value) => {return value === getValues("password") || "Slaptažodis neatitinka"}}})} type="password" className="form-control" placeholder="Pakartoti Slaptažodį" />
                <ErrorMessage errors={errors} name="pakartotas_slaptazodis" render={({ message }) => <p className="text-danger">{message}</p>} />
            </div>

            <button type="submit" className="btn btn-primary btn-block mt-4">Registruotis</button>
        </form>
        </Col>
    </Row>
    </Container>
    )
}
export default Register