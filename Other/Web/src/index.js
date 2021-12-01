import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import LinkBar from "./LinkBar"
import Home from './Home';
import App from "./App"
import {BrowserRouter, Routes, Route } from 'react-router-dom';
import { GlobalProvider } from './Global';
import Login from './Login';
import Register from './Register';


import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import 'semantic-ui-css/semantic.min.css';
import Item from './Item';
import Logout from './Logout';
import Markets from './Markets';



ReactDOM.render(

  <React.StrictMode>
    <GlobalProvider>
    <BrowserRouter>
    <Routes>
        <Route path="/" element={<LinkBar />}>
           <Route index element={<Home />} />
           <Route path="find" element={<App/>} />
           <Route path="register" element={<Register/>}/>
           <Route path="login" element={<Login/>}/>
           <Route path="item" element={<Item/>}/>
           <Route path="logout" element={<Logout/>}/>
           <Route path="markets" element={<Markets/>}/>
        </Route>
    </Routes>
    </BrowserRouter>
    </GlobalProvider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
