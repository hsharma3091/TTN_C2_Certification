import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Support Tickets",
  description: "Support Ticket Management System",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <header className="container" style={{ paddingBottom: 0 }}>
          <h1 style={{ margin: 0, fontSize: "1.35rem" }}>
            <a href="/" style={{ color: "inherit", textDecoration: "none" }}>
              Support Tickets
            </a>
          </h1>
        </header>
        {children}
      </body>
    </html>
  );
}