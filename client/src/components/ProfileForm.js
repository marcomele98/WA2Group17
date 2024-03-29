import React, {useState} from 'react';
import {Form, Button} from 'react-bootstrap';
import {toast} from 'react-toastify';
import API from '../API';
import {useParams, useNavigate} from 'react-router-dom';
import {useEffect} from 'react';
import {errorToast, successToast} from "../utils/Error";

function ProfileForm({setIsLoading}) {

    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");

    const navigate = useNavigate();

    const {userToEditEmail} = useParams();


    useEffect(() => {
        const getProfileFromServer = async () => {
            try {
                setIsLoading(true);
                const res = await API.getCustomerByEmail(userToEditEmail);
                setEmail(res.email);
                setName(res.name);
                setSurname(res.surname);
                setIsLoading(false);
            } catch (err) {
                setIsLoading(false);
                if (err.status == 404)
                    navigate("/profiles")
                else
                    errorToast("Server error");
            }
        };
        if (userToEditEmail) {
            getProfileFromServer()
        }
    }, [userToEditEmail])


    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            setIsLoading(true);
            const newProfile = {
                email: email,
                name: name,
                surname: surname
            }
            userToEditEmail
                ?
                await API.updateProfile(newProfile, userToEditEmail)
                :
                await API.createProfile(newProfile)
            setIsLoading(false);
            successToast(userToEditEmail ? "Profile updated succesfully." : "Profile created successfully.");
            navigate("/profiles")
        } catch (err) {
            setIsLoading(false);
            errorToast(err.detail);
        }
        ;
    }

    return (

        <>
            <h3>{userToEditEmail ? "Edit Profile" : "Create a new Profile"}</h3>

            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Email</Form.Label>
                    <Form.Control type="email" value={email} onChange={ev => setEmail(ev.target.value)}
                                  placeholder="Enter email" readOnly={userToEditEmail} required/>
                </Form.Group>

                <Form.Group className="mb-3" controlId="">
                    <Form.Label>Name</Form.Label>
                    <Form.Control type="text" value={name} onChange={ev => setName(ev.target.value)}
                                  placeholder="Enter Name" required/>
                </Form.Group>

                <Form.Group className="mb-3" controlId="">
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type="text" value={surname} onChange={ev => setSurname(ev.target.value)}
                                  placeholder="Enter Surname" required/>
                </Form.Group>

                <Button variant="primary" type="submit">
                    {userToEditEmail ? "Update" : "Create"}
                </Button>
            </Form>
        </>
    );
}

export {ProfileForm};