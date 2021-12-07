import React, { useContext, useEffect, useState } from "react"
import {Button, Container, Nav, Navbar} from "react-bootstrap"
import { useSpring, animated } from 'react-spring'
import { Link, Outlet} from "react-router-dom"
import Home from "./Home"
import {Context, instance} from "./Global"
import Cookies from 'js-cookie';
import axios from "axios"
import Footer from "./Footer"

function Animation() {
    const styles = useSpring(
        {
        loop: { reverse: true },
        to: {  transform: 'rotateZ(0deg)'}, 
        from: { transform: 'rotateZ(360deg)' } ,
        delay: 1000
      })
      return(
        <animated.div style={styles}><img src="https://www.freeiconspng.com/thumbs/coca-cola-png/bottle-coca-cola-png-transparent-2.png" className="img-fluid" width="100" height="100"></img></animated.div>
    );
}

export default function LinkBar()
{
    const [state, dispatch] = useContext(Context);

    const [loadedUser, setLoadUser] = useState(false);

    useEffect(()=> {
        async function fetchData() {
            let result = await instance.get('api/product/');
            dispatch({type: 'Items', data: result.data});
            result = await instance.get('api/market');
            dispatch({type: 'Markets', data: result.data});
            var cookie = Cookies.get("auth");
            if(cookie)
            {
                const instance = axios.create({
                    baseURL: 'http://localhost:8080/',
                    timeout: 1000,
                    headers: {'Access-Control-Allow-Credentials' : true, 'Authorization' : "Bearer " + cookie}
                  })
              
                  await instance.get('authorize').then(res => {
                      if(res.status === 200)
                      {
                        dispatch({type: 'Active', data:res.data})
                        setLoadUser(true);
                      }
                  }).catch(function (error){
                    setLoadUser(true);
                  })
            }
            else
            {
                setLoadUser(true);
            }
          }
          fetchData();
    
    }, [])
    
    return(
         <div style={{backgroundColor:"slategrey"}}>
           {loadedUser && <div className="h-100" style={{paddingTop:"150px"}}> <nav className="navbar navbar-expand-lg navbar-light bg-dark fixed-top">
            <Navbar.Brand>
            <Animation/>
            </Navbar.Brand>
            <button className="navbar-toggler bg-light" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
               <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarSupportedContent">
              <Nav className="ml-auto">
                  <Link style={{ textDecoration: 'none' }} className="text-light nav-link" to="/">Namai</Link>
                  <Link style={{ textDecoration: 'none' }} className="text-light nav-link" to="find">Ieškoti</Link>
                  <Link style={{ textDecoration: 'none' }} className="text-light nav-link" to="markets">Parduotuvės</Link>
              </Nav>
              {!state.loggedIn &&
              <Nav className="ms-auto">
                          <Link style={{ textDecoration: 'none' }} className="text-light nav-link" to="register">Registruotis</Link>
                          <Link style={{ textDecoration: 'none' }} className="text-light nav-link" to="login">Prisijungti</Link>
              </Nav>
              }
              {state.loggedIn &&
              <Nav className="ms-auto">
                          <Link style={{ textDecoration: 'none' }} className="text-light nav-link" to="logout">Atsijungti</Link>
              </Nav>
              } 
                  </div>
              </nav>
        
            <Outlet/>

        </div>
        }
            <div style={{paddingBottom:"150px", overflow:"auto"}}>
            </div>
            <Footer/>
            </div>

    );
}