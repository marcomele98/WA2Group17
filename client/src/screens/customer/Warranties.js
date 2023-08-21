import {useWarrantiesVM} from "../../presenters/customer/WarrantiesVM";
import {WarrantyCard} from "../../components/WarrantyCard";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {useState} from "react";

export const Warranties = () => {
    const warrantiesVM = useWarrantiesVM();
    const navigate = useNavigate();

    return (
        <>
            {
                warrantiesVM.warranties.map(warranty => <WarrantyCard warranty={warranty} ActionElement={<Button
                    onClick={() => navigate(warranty.id.toString())}>details</Button>}/>)
            }
        </>
    );
}