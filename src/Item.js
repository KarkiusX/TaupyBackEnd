import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { Form, Modal, Button } from "react-bootstrap";
import { useLocation } from "react-router";
import { Comment, List } from "semantic-ui-react";
import { Context } from "./Global";
import CommentTemplate from "./Info";

export default function Item() {
    const {state} = useLocation();

    const [states, dispatch] = useContext(Context);

    const [comments, setComments] = useState();

    const [show, setShow] = useState(false);

    const [update, updateComments] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => {
        setShow(true);
    }
    const instance = axios.create({
        baseURL: 'https://taupyk.herokuapp.com/api',
        timeout: 1000,
        headers: {
            'Content-Type': 'application/json'
        }
      })
    console.log(states.userinfo === undefined);
    useEffect(() => {
        async function GetComments()
        {
              var Endpoint = "comment/" + state.uid;
              await instance.get(Endpoint).then(res => {
                  if(res.status === 200)
                  {
                      setComments(res.data);
                      console.log(res);
    
                  }
              }).catch(function (error){
              })
        }
        GetComments();
    }, [show, update])
    async function UpdateComments(comment)
    {
        var data = new Object();
        data.text = comment;
        data.user = {"uid": states.userinfo.uid}
        data.product = {"uid": parseInt(state.uid)}

        var dataJson = JSON.stringify(data);
        var Endpoint = "comment/"
        await instance.post(Endpoint, dataJson).then(res => {
            handleClose();
        }).catch(function (error){
        })
    }
    const PostComment = (e) =>
    {
        e.preventDefault();

        UpdateComments(e.target[0].value)

    }
    return(
        <div>
      <div className="d-flex justify-content-center pt-5 ">
           <div className="d-flex flex-row">
                   <img src={state.imageProduct + '.png'} className="img-fluid" height="150" width="150"/>
                   <h3>Price: {state.price}€</h3>
           </div>
       </div>
       <div className="d-flex justify-content-center pt-5">
                {states.loggedIn &&
                <div className="px-3">
                     <Button variant="primary" onClick={handleShow}>
                            Pridėti komentarą
                     </Button>
                </div>
                }
       </div>
       <div className="d-flex justify-content-center pt-5">
                <List style={{overflow: 'auto', maxHeight: 200, maxWidth: 300}}>
             <Comment.Group>
              {comments != null && comments.length >= 1 && comments.map((comment) =>(
                        <CommentTemplate id={`${comment.uid}`} name={`${comment.user.username}`} message={`${comment.text}`} time={`${comment.timeStamp}`} update={updateComments}></CommentTemplate>
                ))}
            </Comment.Group>
            </List>
       </div>
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Parašykite komentarą</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={PostComment}>
                    <input id="komentaras" type="text" placeholder="Parašykite komentarą"></input>
                    <button type="submit" className="btn btn-primary">Komentuoti</button>
                </Form>
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