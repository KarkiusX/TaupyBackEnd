
import React from "react"
import { Container, Row, Col } from "react-bootstrap";

class Home extends React.Component{

    render()
    {
        return(
            <Container className="h-100">
                <Row className="h-25 pt-5 justify-content-center align-items-center">
                     <Col md={{ span: 5, offset: 1 }}>
                         <h2>Kodėl rinktis mus?</h2>
                         <h4>Skirtingos parduotuvės teikia vienoda prekę, tačiau reikėtu keliatu po parduotuves, ieškoti prekės ir dar pažėjus į kaina pagalvoti ar apsimoka pirkti, kad jums to nereikėtu daryti, viska padarome mes, parodome prekę, kaina ir netgi parduotuvę, sutaupote laiko bei pinigų </h4>
                     </Col>
                </Row>
            </Container>
        );
    }
}
export default Home;