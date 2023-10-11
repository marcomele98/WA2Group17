import { useEffect, useState } from "react";
import API from "../API";
import { errorToast, successToast } from "../utils/Error";

export const useTicketVM = (onError, id) => {
    const [ticket, setTicket] = useState()
    const [loading, setLoading] = useState(true);

    const addAttachment = async (attachmentId, messageId, ticket) => {
        const attachment = await API.downloadAttachment(attachmentId);
        const newMessages = ticket.messages.map(message => {
            if (message.id === messageId) {
                const newAttachments = message.attachments ? [...message.attachments, attachment] : [attachment]
                return { ...message, attachments: newAttachments }
            }
            return message
        })
        return { ...ticket, messages: newMessages }
    }
    
    const getTicket = async () => {
        try {
            let result = await API.getTicket(id);
            for(let message of result.messages){
                for(let attachmentId of message.attachmentIds){
                    result = await addAttachment(attachmentId, message.id, result);
                }
            }
            console.log(result)
            setTicket(result);
        } catch (e) {
            errorToast("Error while retrieving ticket " + id);
            switch (e.status) {
                default:
                    onError("Error while searching ticket " + id);
            }
        }
    }

    const closeTicket = async (id) => {
        try {
            await API.closeTicket(id);
            setLoading(true);
            successToast("Ticket closed successfully");
        } catch (e) {
            errorToast("Error while closing ticket " + id);
            switch (e.status) {
                default:
                    onError("Error while closing ticket " + id);
            }
        }
    }

    const reopenTicket = async (id ) => {
        try {
            await API.reopenTicket(id);
            setLoading(true);
            successToast("Ticket reopened successfully");
        } catch (e) {
            errorToast("Error while reopening ticket " + id);
            switch (e.status) {
                default:
                    onError("Error while reopening ticket " + id);
            }
        }
    }

    const resolveTicket = async (id) => {
        try {
            await API.resolveTicket(id);
            setLoading(true);
            successToast("Ticket resolved successfully");
        } catch (e) {
            errorToast("Error while resolving ticket " + id);
            switch (e.status) {
                default:
                    onError("Error while resolving ticket " + id);
            }
        }
    }

    useEffect(() => {
        if(loading) {
            getTicket();
            setLoading(false);
        }
    }, [loading]);

    return { ticket, getTicket, closeTicket, reopenTicket, resolveTicket }
}