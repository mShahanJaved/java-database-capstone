/*
 * ============================================================
 * FOOTER.JS — Reusable Footer Component
 * ============================================================
 * The footer is the SAME on every page.
 * It contains: Company info, Support links, Legal links.
 * 
 * Unlike the header, the footer doesn't change based on role.
 * It's always the same — that's why we keep it simple.
 */

/*
 * renderFooter() — Build and inject the footer HTML.
 * 
 * Called on every page that has a <div id="footer"></div>.
 */
function renderFooter() {
    const footer = document.getElementById("footer");
    if (!footer) return;

    footer.innerHTML = `
        <footer class="footer">
            <div class="footer-column">
                <h4>Company</h4>
                <a href="#">About</a>
                <a href="#">Careers</a>
                <a href="#">Press</a>
            </div>
            <div class="footer-column">
                <h4>Support</h4>
                <a href="#">Account</a>
                <a href="#">Help Center</a>
                <a href="#">Contact</a>
            </div>
            <div class="footer-column">
                <h4>Legals</h4>
                <a href="#">Terms & Conditions</a>
                <a href="#">Privacy Policy</a>
                <a href="#">Licensing</a>
            </div>
        </footer>
        <div style="text-align: center; padding: 15px; background: #1a1a2e; color: #666; font-size: 0.85rem;">
            © ${new Date().getFullYear()} Hospital CMS. All rights reserved.
        </div>
    `;
}

/*
 * INITIALIZE:
 * Call renderFooter() when this script loads.
 */
renderFooter();
