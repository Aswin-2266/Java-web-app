const fileInput = document.getElementById("fileInput");
const dragArea = document.getElementById("dragArea");
let selectedFile;

dragArea.addEventListener("click", () => fileInput.click());
fileInput.addEventListener("change", () => {
    selectedFile = fileInput.files[0];
    const fileInfo = document.getElementById("selectedFileInfo");
    if (selectedFile) {
        const fileSize = (selectedFile.size / 1024).toFixed(2);
        fileInfo.innerHTML = `<strong>Selected:</strong> ${selectedFile.name} (${fileSize} KB)`;
        fileInfo.classList.add("text-primary");
    } else {
        fileInfo.innerHTML = "";
        fileInfo.classList.remove("text-primary");
    }
});

async function uploadFile() {
    if (!selectedFile) {
        alert("Please select a file to upload.");
        return;
    }
    let formData = new FormData();
    formData.append("file", selectedFile);
    const uploadFeedback = document.getElementById("uploadFeedback");
    uploadFeedback.classList.remove("d-none");
    try {
        const response = await fetch("/s3/upload", {
            method: "POST",
            body: formData
        });
        if (response.ok) {
            const result = await response.json();
            alert(result.message || "File uploaded successfully!");
            selectedFile = null;
            fileInput.value = "";
            document.getElementById("selectedFileInfo").innerHTML = "";
            loadFiles();
        } else {
            alert("Upload failed.");
        }
    } catch (error) {
        alert("Error: " + error.message);
    } finally {
        uploadFeedback.classList.add("d-none");
    }
}

async function loadFiles() {
    try {
        const response = await fetch("/s3/files");
        const files = await response.json();
        const fileList = document.getElementById("fileList");
        fileList.innerHTML = files.map(file => `
            <div class="file-item">
                <span class="text-truncate" style="max-width: 200px;">${file}</span>
                <div>
                    <button class="btn btn-success btn-sm" onclick="downloadFile('${file}')">Download</button>
                    <button class="btn btn-primary btn-sm ms-1" onclick="previewFile('${file}')">Preview</button>
                    <button class="btn btn-danger btn-sm ms-1" onclick="deleteFile('${file}')">Delete</button>
                </div>
            </div>
        `).join("");
    } catch (error) {
        document.getElementById("fileList").innerHTML = `<p class="text-danger">Error loading files: ${error.message}</p>`;
    }
}

async function downloadFile(fileName) {
    try {
        const response = await fetch(`/s3/download/${fileName}`);
        const presignedUrl = await response.text();
        const link = document.createElement("a");
        link.href = presignedUrl;
        link.download = fileName;
        link.click();
    } catch (error) {
        alert("Error downloading file: " + error.message);
    }
}

async function previewFile(fileName) {
    try {
        const response = await fetch(`/s3/download/${fileName}`);
        const presignedUrl = await response.text();
        document.getElementById("filePreview").src = presignedUrl;
        const modal = new bootstrap.Modal(document.getElementById("previewModal"));
        modal.show();
    } catch (error) {
        alert("Error previewing file: " + error.message);
    }
}

function closePreview() {
    document.getElementById("filePreview").src = "";
    const modal = bootstrap.Modal.getInstance(document.getElementById("previewModal"));
    modal.hide();
}

async function deleteFile(fileName) {
    if (!confirm(`Delete "${fileName}"?`)) return;
    try {
        const response = await fetch(`/s3/delete/${fileName}`, {
            method: "DELETE"
        });
        if (response.ok) {
            const result = await response.json();
            alert(result.message || "File deleted successfully!");
            loadFiles();
        } else {
            alert("Failed to delete file.");
        }
    } catch (error) {
        alert("Error deleting file: " + error.message);
    }
}

window.onload = loadFiles;