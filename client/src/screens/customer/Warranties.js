import {useWarrantiesVM} from "../../presenters/customer/WarrantiesVM";
import {WarrantyCard} from "../../components/WarrantyCard";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";

export const Warranties = () => {
    const warrantiesVM = useWarrantiesVM();
    const navigate = useNavigate();

    return (
        <>
            {
                warrantiesVM.warranties.map(warranty => <WarrantyCard warranty={warranty}
                                                                      onAction={() => navigate(warranty.id.toString())}/>)
            }
            {
                warrantiesVM.warranties.length === 0 && (
                    <h2 className="text-2xl">No warranties</h2>
                )
            }
        </>
    );
}