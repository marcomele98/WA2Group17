import {useEffect, useReducer} from "react";
import API from "../../API";

const emptyMessage = {
    text: '',
    attachmentIds: {}
}

const emptyTicket = {
    title: '',
    problemType: '',
    warrantyId: '',
    initialMessage: emptyMessage
}

const ticketReducer = (state, action) => {
    switch (action.type) {
        case 'SET_TITLE':
            return {...state, title: action.payload}
        case 'SET_PROBLEM_TYPE':
            return {...state, problemType: action.payload}
        case 'SET_WARRANTY_ID':
            return {...state, warrantyId: action.payload}
        case 'SET_DESCRIPTION':
            return {...state, initialMessage: {...state.initialMessage, text: action.payload}}
        case 'ADD_ATTACHMENT':
            return {...state,
                initialMessage: {
                    ...state.initialMessage,
                    attachmentIds: {...state.initialMessage.attachmentIds, ...action.payload}
                }
            }
        case 'REMOVE_ATTACHMENT':
            let attachmentIds = {...state.initialMessage.attachmentIds}
            delete attachmentIds[action.payload]
            return {...state,
                initialMessage: {
                    ...state.initialMessage,
                    attachmentIds: attachmentIds
                }
            }
        case 'RESET':
            return emptyTicket
        default:
            return state
    }
}
export const useCreateTicketVM = (warrantyId) => {
    const [ticket, dispatch] = useReducer(ticketReducer, emptyTicket)
    useEffect(() => { //TODO: valuto la possibilità di aggiungerli anche selezionando la  warranty, in qual caso vado a chiederle tutte al server
        dispatch({type: 'SET_WARRANTY_ID', payload: warrantyId})
    }, []);
    const setTitle = (title) => dispatch({type: 'SET_TITLE', payload: title})
    const setProblemType = (problemType) => dispatch({type: 'SET_PROBLEM_TYPE', payload: problemType})
    const setDescription = (description) => dispatch({type: 'SET_DESCRIPTION', payload: description})
    const addAttachment = async (attachment) => {
        if(!attachment) return
        try {
            const id = await API.uploadAttachment(attachment)
            let payload = {}
            payload[id] = attachment
            dispatch({type: 'ADD_ATTACHMENT', payload: payload})
        } catch (e) {
            throw "Error while uploading attachment"
        }
    }

    const removeAttachment = async (attachment) => {
        //TODO: chiamo il server per rimuovere l'attachment (se pensiamo di farlo)
        dispatch({type: 'REMOVE_ATTACHMENT', payload: attachment})
    }

    const reset = () => dispatch({type: 'RESET'})

    const save = async () => {
        //TODO: validazione
        try {
            let ticketDTO = {...ticket}
            ticketDTO.initialMessage.attachmentIds = Object.keys(ticket.initialMessage.attachmentIds)
            await API.createTicket(ticketDTO)
        } catch (e) {
            switch (e?.response?.status) {
                default: //TODO: gestire gli altri errori e gestisco il messaggio corretto
                    throw "Server error"
            }
        }
    }

    return {
        title: ticket.title,
        problemType: ticket.problemType,
        warrantyId: ticket.warrantyId,
        description: ticket.initialMessage.text,
        attachments: ticket.initialMessage.attachmentIds,
        setTitle,
        setProblemType,
        setDescription,
        addAttachment,
        removeAttachment,
        reset,
        save
    }

}