export function formatDate(inputDateStr) {
  const inputDate = new Date(inputDateStr);
  const month = String(inputDate.getMonth() + 1).padStart(2, "0");
  const day = String(inputDate.getDate()).padStart(2, "0");
  const year = inputDate.getFullYear();
  const hours = String(inputDate.getHours()).padStart(2, "0");
  const minutes = String(inputDate.getMinutes()).padStart(2, "0");

  return `${month}/${day}/${year} ${hours}:${minutes}`;
}
