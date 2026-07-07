import type { Metadata } from "next";
import { ItemDetailPage } from "@/components/item-detail-page";
import { getItem } from "@/lib/api";

type ItemPageProps = {
  params: Promise<{
    id: string;
  }>;
};

export default async function ItemPage({ params }: Readonly<ItemPageProps>) {
  const { id } = await params;
  return <ItemDetailPage id={id} />;
}

export async function generateMetadata({ params }: Readonly<ItemPageProps>): Promise<Metadata> {
  try {
    const { id } = await params;
    const item = await getItem(id);
    return {
      title: `${item.title} | Vertex`,
      description: item.summary,
      openGraph: {
        title: item.title,
        description: item.summary,
        type: "article",
        images: ["/icon.svg"]
      },
      twitter: {
        card: "summary",
        title: item.title,
        description: item.summary,
        images: ["/icon.svg"]
      }
    };
  } catch {
    return {
      title: "Vertex Item",
      description: "Ranked developer intelligence item"
    };
  }
}
