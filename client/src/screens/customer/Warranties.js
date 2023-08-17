import {useWarrantiesVM} from "../../presenters/customer/WarrantiesVM";
import {WarrantyCard} from "../../components/WarrantyCard";
import {Button} from "react-bootstrap";

export const Warranties = () => {
    const warrantiesVM = useWarrantiesVM();
    return (
        <>
            {
                warrantiesVM.warranties.map(warranty => <WarrantyCard warranty={warranty} ActionElement={<Button>details</Button>}/>)
            }
        </>
    );
}