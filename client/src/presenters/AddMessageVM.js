import { useEffect, useReducer } from "react";
import API from "../API";

const emptyMessage = {
    text: '',
    attachmentIds: {}
}


const messageReducer = (state, action) => {
    switch (action.type) {
        case 'SET_DESCRIPTION':
            return { ...state, text: action.payload }
        case 'ADD_ATTACHMENT':
            return {
                ...state,
                attachmentIds: { ...state.attachmentIds, ...action.payload }
            }
        case 'REMOVE_ATTACHMENT':
            let attachmentIds = { ...state.attachmentIds }
            delete attachmentIds[action.payload]
            return {
                ...state,
                attachmentIds: attachmentIds
            }
        case 'RESET':
            return emptyMessage
        default:
            return state
    }
}

export const useAddMessageVM = () => {
    const [message, dispatch] = useReducer(messageReducer, emptyMessage)
    const setDescription = (description) => dispatch({ type: 'SET_DESCRIPTION', payload: description })
    const addAttachment = async (attachment) => {
        if (!attachment) return
        try {
            const id = await API.uploadAttachment(attachment)
            let payload = {}
            payload[id] = attachment
            dispatch({ type: 'ADD_ATTACHMENT', payload: payload })
        } catch (e) {
            throw "Error while uploading attachment"
        }
    }

    const removeAttachment = async (attachment) => {
        //TODO: chiamo il server per rimuovere l'attachment (se pensiamo di farlo)
        dispatch({ type: 'REMOVE_ATTACHMENT', payload: attachment })
    }

    const reset = () => dispatch({ type: 'RESET' })

    const save = async (id) => {
        //TODO: validazione
        try {
            console.log(message)
            let messageDTO = { ...message }
            console.log(messageDTO)
            messageDTO.attachmentIds = Object.keys(message.attachmentIds)
            await API.addMessage(messageDTO, id)
        } catch (e) {
            switch (e?.response?.status) {
                default: //TODO: gestire gli altri errori e gestisco il messaggio corretto
                    throw "Server error"
            }
        }
    }

    return {
        description: message.text,
        attachments: message.attachmentIds,
        setDescription,
        addAttachment,
        removeAttachment,
        reset,
        save
    }
}