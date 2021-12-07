import {Comment} from "semantic-ui-react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrashAlt, faEdit} from '@fortawesome/free-solid-svg-icons'
import { Modal, Button } from "react-bootstrap";
import { useContext, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { Context } from "./Global";


export default function CommentTemplate ({id, name, message, time, update})
{
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

  

  const DeletePost = async () => {
      var Endpoint = "/comment/"+id;
      console.log(Endpoint);
      await instance.delete(Endpoint).then(() =>
      {
          update(true);
          handleClose();
      });
  }

  return(
    <div>
    <Comment>
    <Comment.Content>
      <Comment.Author as='a'>{name}</Comment.Author>
      <Comment.Metadata>
        <div>{new Date(parseInt(time * 1000)).toDateString()}</div>
      </Comment.Metadata>
      <Comment.Text>{message}</Comment.Text>
    </Comment.Content>
    {states.userinfo.admin && 
    <FontAwesomeIcon icon={faTrashAlt} style={{width: "15px", height : "15px"}} onClick={handleShow}/>}
    </Comment>
    <Modal show={show} onHide={handleClose}>
    <Modal.Header closeButton>
      <Modal.Title>Komentaro ištrinimas</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <h3>Tikrai norite ištrinti?</h3>
    </Modal.Body>
    <Modal.Footer>
      <Button variant="secondary" onClick={handleClose}>
      Uždaryti
      </Button>
      <Button variant="primary" onClick={DeletePost}>
      Ištrinti
      </Button>
    </Modal.Footer>
    </Modal>
      </div>
  )

}
  