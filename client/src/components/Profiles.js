import API from '../API';
import { toast } from "react-toastify";
import { useState } from "react";
import { Card, Row, Col, InputGroup, FormControl, Button, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import { PlusSquareFill } from "react-bootstrap-icons";
import { ClickableOpacity } from './ClickableOpacity';
import { useNavigate } from "react-router-dom";



function Profiles({ setIsLoading }) {

    const [profile, setProfile] = useState();
    const [email, setEmail] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            setIsLoading(true);
            let res = await API.getProfileByEmail(email)
            setProfile(res);
            setIsLoading(false);
        }
        catch (err) {
            setIsLoading(false);
            setProfile(undefined);
            if (err.status == 404)
                toast.error("Profile not found.", { position: "top-center" }, { toastId: 4 });
            else
                toast.error("Server error.", { position: "top-center" }, { toastId: 4 });
        }
    }



    return (
        <>
            <Row>
                <Col xs="auto" sm="auto" md="auto" lg="auto" xl="auto" xxl="auto">
                    <ClickableOpacity onClick={_ => navigate("/create-profile")}>
                        <PlusSquareFill size={35} className="plus" fill='#198753' />
                    </ClickableOpacity>
                </Col>
                <Col style={{width:"100%"}}>
                    <Form onSubmit={handleSubmit}>
                        <InputGroup>
                            <FormControl type="text" placeholder="Search" onChange={ev => setEmail(ev.target.value)} value={email} className="mr-sm-2" />
                            <Button variant="outline-success" type='submit'>Search</Button>
                        </InputGroup>
                    </Form>
                </Col>
            </Row>

            {profile ?
                <>
                    <div className='mt-3'>
                        <Card className='card' key={profile.email}>
                            <Card.Header>{profile.email}
                                <Link to={"/edit-profile/" + profile.email} className="details"><div>
                                    Edit
                                </div></Link>

                            </Card.Header>
                            <Card.Body>
                                <Card.Text>{"Name: " + profile.name}</Card.Text>
                                <Card.Text>{"Surname: " + profile.surname}</Card.Text>
                            </Card.Body>
                        </Card>
                    </div>
                </> :
                <>
                </>
            }
        </>
    )

}


export { Profiles };