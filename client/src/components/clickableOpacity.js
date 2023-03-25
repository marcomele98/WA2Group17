import { useState } from "react";
import { Button } from "react-bootstrap";

const OPACITY_WHEN_MOUSE_INTERACT = 0.6;
const NORMAL_OPACITY = 0.9;

function ClickableOpacity({ onClick, children, style, disabled, className, id, type}) {
  const [opacity, setOpacity] = useState(NORMAL_OPACITY);
  return (
    <Button
      variant="link"
      className={"text-white shadow-none text-decoration-none "+className}
      id={id}
      disabled={disabled}
      onClick={onClick}
      type={type}
      style={{
        ...style,
        opacity: opacity,
        padding:"0%",
        margin:"0%",
        width:"fit-content",
      }}
      onMouseOver={
        disabled ? ()=>{} : () => setOpacity(OPACITY_WHEN_MOUSE_INTERACT)
      }
      onMouseLeave={disabled ? ()=>{} : () => setOpacity(NORMAL_OPACITY)}
      onMouseDown={disabled ? ()=>{} : () => setOpacity(1)}
      onMouseUp={
        disabled ? ()=>{} : () => setOpacity(OPACITY_WHEN_MOUSE_INTERACT)
      }
    >
      {children}
    </Button>
  );
}

export { ClickableOpacity };
