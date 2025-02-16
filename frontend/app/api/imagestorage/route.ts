import { NextResponse } from 'next/server';
import fs from 'fs';
import path from 'path';

export async function POST(req: Request) {
  try {
    const { imageUrl } = await req.json();

    if (!imageUrl) {
      return NextResponse.json(
        { error: "L'URL de l'image est requise" },
        { status: 400 }
      );
    }

    const response = await fetch(imageUrl);
    if (!response.ok) {
      return NextResponse.json(
        { error: "Impossible de télécharger l'image à partir de l'URL fournie" },
        { status: 400 }
      );
    }

    const buffer = await response.arrayBuffer();

    const tempDir = path.join(process.cwd(), 'public', 'imagetemp');
    const fileName = `image_${Date.now()}.png`;
    const filePath = path.join(tempDir, fileName);

    if (!fs.existsSync(tempDir)) {
      fs.mkdirSync(tempDir, { recursive: true });
    }

    fs.writeFileSync(filePath, Buffer.from(buffer));

    const fileUrl = `/imagetemp/${fileName}`;
    return NextResponse.json({ fileUrl });
  } catch (error) {
    console.error('Erreur:', error);
    return NextResponse.json(
      { error: 'Erreur lors du traitement de la requête' },
      { status: 500 }
    );
  }
}

// Gestion des requêtes DELETE (Suppression d'un fichier temporaire)
export async function DELETE(req: Request) {
  try {
    const { searchParams } = new URL(req.url);
    const fileName = searchParams.get('fileName');

    if (!fileName) {
      return NextResponse.json(
        { error: "Le nom du fichier est requis pour la suppression" },
        { status: 400 }
      );
    }

    const tempDir = path.join(process.cwd(), 'public', 'imagetemp');
    const filePath = path.join(tempDir, fileName);

    if (!fs.existsSync(filePath)) {
      return NextResponse.json(
        { error: "Le fichier n'existe pas ou a déjà été supprimé" },
        { status: 404 }
      );
    }

    fs.unlinkSync(filePath);

    return NextResponse.json({ message: "Fichier supprimé avec succès" });
  } catch (error) {
    console.error('Erreur lors de la suppression du fichier:', error);
    return NextResponse.json(
      { error: 'Erreur lors de la suppression du fichier' },
      { status: 500 }
    );
  }
}

// Gestion des requêtes GET (Récupération des informations d'un fichier temporaire)
export async function GET(req: Request) {
  try {
    const { searchParams } = new URL(req.url);
    const fileName = searchParams.get('fileName');

    if (!fileName) {
      return NextResponse.json(
        { error: "Le nom du fichier est requis" },
        { status: 400 }
      );
    }

    const tempDir = path.join(process.cwd(), 'public', 'imagetemp');
    const filePath = path.join(tempDir, fileName);

    if (!fs.existsSync(filePath)) {
      return NextResponse.json(
        { error: "Le fichier demandé n'existe pas" },
        { status: 404 }
      );
    }

    const fileStats = fs.statSync(filePath);

    // Lire le contenu du fichier
    const fileBuffer = fs.readFileSync(filePath);
    const fileMimeType = getMimeType(filePath); // Fonction pour déterminer le type MIME du fichier

    return new Response(fileBuffer, {
      headers: {
        'Content-Type': fileMimeType,
        'Content-Disposition': `attachment; filename="${fileName}"`,
        'Content-Length': fileStats.size.toString(),
      },
    });
  } catch (error) {
    console.error('Erreur lors de la récupération du fichier:', error);
    return NextResponse.json(
      { error: 'Erreur lors de la récupération du fichier' },
      { status: 500 }
    );
  }
}

// Fonction pour obtenir le type MIME en fonction de l'extension du fichier
function getMimeType(filePath: string): string {
  const ext = path.extname(filePath).toLowerCase();
  switch (ext) {
    case '.jpg':
    case '.jpeg':
      return 'image/jpeg';
    case '.png':
      return 'image/png';
    case '.gif':
      return 'image/gif';
    default:
      return 'application/octet-stream'; // Par défaut pour les fichiers inconnus
  }
}
